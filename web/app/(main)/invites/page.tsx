import {
  Empty,
  EmptyDescription,
  EmptyHeader,
  EmptyTitle,
} from "@/components/ui/empty";
import { client } from "@/lib/api-client";
import { components } from "@/types/api";
import { Invite } from "./components/Invite";
import { InviteList } from "./components/InviteList";

type Invite = components["schemas"]["ReceivedInviteResponse"];

export default async function Page() {
  const { error, data } = await client.GET("/invites");

  if (error) {
    throw new Error("Could not get your invites");
  }

  const respondableInvites: Invite[] = [];
  const nonRespondableInvites: Invite[] = [];

  data.forEach((invite) => {
    (invite.canRespond ? respondableInvites : nonRespondableInvites).push(
      invite
    );
  });

  if (respondableInvites.length === 0 && nonRespondableInvites.length === 0) {
    return (
      <Empty>
        <EmptyHeader>
          <EmptyTitle>No invites yet</EmptyTitle>
          <EmptyDescription>
            This page is where you would see invites from another user inviting
            you to join a challenge.
          </EmptyDescription>
        </EmptyHeader>
      </Empty>
    );
  }

  return (
    <div className="divide-y ">
      {respondableInvites.length > 0 && (
        <div className="py-4">
          <h1 className="mb-4">Invites you have received</h1>
          <InviteList invites={respondableInvites} />
        </div>
      )}
      {nonRespondableInvites.length > 0 && (
        <div className="py-4">
          <div className="mb-4">
            <h1>Invites you can no longer respond to</h1>
            <p>These challenges have already been started</p>
          </div>
          <InviteList invites={nonRespondableInvites} />
        </div>
      )}
    </div>
  );
}
