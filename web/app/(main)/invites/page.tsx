import { client } from "@/lib/api-client";
import { components } from "@/types/api";
import { NonRespondableInvite } from "./components/non-respondable-invite";
import { RespondableInvite } from "./components/respondable-invite";

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
        <div>
          {respondableInvites.map((invite) => (
            <RespondableInvite key={invite.id} invite={invite} />
          ))}
        </div>
      </div>
      <div>
        <h1>Invites you can no longer respond to</h1>
        <p>These challenges have already been started</p>
        {nonRespondableInvites.map((invite) => (
          <NonRespondableInvite key={invite.id} invite={invite} />
        ))}
      </div>
    </div>
  );
}
