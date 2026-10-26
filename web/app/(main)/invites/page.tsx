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

  return (
    <div>
      <div>
        <h1>Your invites</h1>
        <InviteList invites={respondableInvites} />
      </div>
      <div>
        <h1>Invites you can no longer respond to</h1>
        <p>These challenges have already been started</p>
        <InviteList invites={nonRespondableInvites} />
      </div>
    </div>
  );
}
