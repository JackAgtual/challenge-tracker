import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { getChallengeNameAndDuration } from "@/lib/name-utils";
import { components } from "@/types/api";
import { InvitedBy } from "../InvitedBy";
import { DeleteButton } from "./DeleteButton";

type NonRespondableInviteProps = {
  invite: components["schemas"]["ReceivedInviteResponse"];
};

export function NonRespondableInvite({ invite }: NonRespondableInviteProps) {
  const { id, challenge, inviteSender } = invite;
  return (
    <Card key={id}>
      <CardHeader>
        <CardTitle>{getChallengeNameAndDuration(challenge)}</CardTitle>
        <CardAction>
          <DeleteButton inviteId={id} />
        </CardAction>
      </CardHeader>
      <CardContent className="flex flex-col gap-y-2">
        <InvitedBy user={inviteSender} />
        <p>Challenge started: {challenge.starDate ?? "Unknown"}</p>
      </CardContent>
    </Card>
  );
}
